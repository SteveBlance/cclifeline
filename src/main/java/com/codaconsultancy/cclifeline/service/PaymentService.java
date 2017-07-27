package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentReferenceRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentReferenceRepository paymentReferenceRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> findAllUnmatchedPayments() {
        return paymentRepository.findByMemberIsNull();
    }

    public List<Payment> findAllMatchedPayments() {
        return paymentRepository.findByMemberIsNotNull();
    }

    public Payment savePayment(Payment payment) {
        reactivatePayerMembership(payment);
        return paymentRepository.save(payment);
    }

    public PaymentReference savePaymentReference(PaymentReference paymentReference) {
        return paymentReferenceRepository.save(paymentReference);

    }

    public List<Payment> parsePayments(String contents, String filename) throws IOException, NumberFormatException, ArrayIndexOutOfBoundsException {
        List<Payment> payments = new ArrayList<>();
        if (filename.endsWith(".csv") || filename.endsWith(".CSV")) {
            payments = getPaymentsFromCsvFile(contents);
        }
        return payments;
    }

    private List<Payment> getPaymentsFromCsvFile(String contents) throws IOException, ArrayIndexOutOfBoundsException {
        List<Payment> payments = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(contents, format);

        for (CSVRecord record : parser) {
            String paymentDateText = record.get(0);
            Date date = DateTime.parse(paymentDateText, fmt).toDate();
            String transactionType = record.get(4);
            String accountNumber = record.get(2);
            String description = record.get(8);
            String creditAmountText = record.get(7);
            String name = record.get(9);
            if (transactionType.equalsIgnoreCase("CR") && (!creditAmountText.isEmpty())) {
                Float creditAmount = Float.parseFloat(creditAmountText);
                Payment payment = new Payment(date, creditAmount, description, accountNumber, name);
                matchWithMember(payment);
                payments.add(payment);
            }
        }
        return payments;
    }

    private void matchWithMember(Payment payment) {
        boolean paymentMatched = tryMatchByMembershipNumberAndName(payment);
        if (!paymentMatched) {
            tryMatchByFullPaymentReference(payment);
        }

    }

    private void tryMatchByFullPaymentReference(Payment payment) {
        String reference = (payment.getCreditReference() != null) ? payment.getCreditReference() : "";
        String name = (payment.getName() != null) ? payment.getName() : "";

        List<PaymentReference> allReferences = paymentReferenceRepository.findAll();
        for (PaymentReference paymentReference : allReferences) {
            if (reference.equalsIgnoreCase(paymentReference.getReference()) && name.equalsIgnoreCase(paymentReference.getName())) {
                payment.setMember(paymentReference.getMember());
            }
        }
    }

    private boolean tryMatchByMembershipNumberAndName(Payment payment) {
        boolean isMatched = false;
        String reference = payment.getCreditReference();
        String name = payment.getName();
        String fullDescription = (reference + name).toUpperCase();
        List<Member> allMembers = memberRepository.findAllByOrderBySurnameAscForenameAsc();
        for (Member member : allMembers) {
            if (member.getMembershipNumber().equals(findMembershipNumberInReference(reference)) && (fullDescription.contains(member.getSurname().toUpperCase()) || fullDescription.contains(member.getForename().toUpperCase()))) {
                payment.setMember(member);
                isMatched = true;
                break;
            }
        }
        return isMatched;
    }

    private Long findMembershipNumberInReference(String paymentReference) {
        Pattern pattern = Pattern.compile("(\\s\\d{2,5}($|\\s|-|\\/))");
        Matcher matcher = pattern.matcher(paymentReference);
        String membershipNumberText = "";
        if (matcher.find()) {
            membershipNumberText = matcher.group(1).trim();
        }
        membershipNumberText = membershipNumberText.replace("/", "").replace("-", "");
        return membershipNumberText.isEmpty() ? 0L : Long.parseLong(membershipNumberText);
    }

    public List<Payment> savePayments(List<Payment> payments) {
        for (Payment payment : payments) {
            reactivatePayerMembership(payment);
        }
        paymentRepository.save(payments);
        return payments;
    }

    public Payment findById(Long id) {
        return paymentRepository.findOne(id);
    }

    public Payment updatePayment(Payment payment) {
        reactivatePayerMembership(payment);
        return paymentRepository.save(payment);
    }

    public List<Payment> findPaymentsForMember(Member member) {
        return paymentRepository.findByMember(member);
    }

    public Payment findLatestPayment(Member member) {
        return paymentRepository.findTopByMemberOrderByPaymentDateDesc(member);
    }

    public List<Member> findPossiblePayers(Payment payment) {
        List<Member> possiblePayers = new ArrayList<>();
        String creditReference = payment.getCreditReference();
        String name = payment.getName().toUpperCase().replace("MR ", " ").replace("MRS ", " ").replace("MISS ", " ").replace("DR ", " ");
        Long membershipNumber = findMembershipNumberInReference(creditReference);
        Member membershipNumberMatch = memberRepository.findByMembershipNumber(membershipNumber);
        if (null != membershipNumberMatch) {
            possiblePayers.add(membershipNumberMatch);
        }
        String fullDescription = (creditReference + " " + name).toUpperCase().replace("FPS ", " ").replace("CREDIT ", " ").replace("BANK GIRO ", " ");
        StringTokenizer tokenizer = new StringTokenizer(fullDescription, " ");
        while (tokenizer.hasMoreElements()) {
            String word = tokenizer.nextElement().toString();
            List<Member> surnameMatch = memberRepository.findAllBySurnameIgnoreCaseAndStatusOrderByForename(word, "Open");
            possiblePayers.addAll(surnameMatch);
        }

        return possiblePayers;
    }

    public void deletePayment(Long id) {
        paymentRepository.delete(id);
    }


    private void reactivatePayerMembership(Payment payment) {
        Member member = payment.getMember();
        if (null != member && !member.getStatus().equalsIgnoreCase("Open")) {
            member.setStatus("Open");
            memberRepository.save(member);
        }
    }
}
