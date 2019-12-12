package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentReferenceRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
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
public class PaymentService extends LifelineService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentReferenceRepository paymentReferenceRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<PaymentViewBean> findAllPayments() {
        return paymentRepository.findAllPayments();
    }

    public List<PaymentViewBean> findAllUnmatchedPayments() {
        return paymentRepository.findUnmatchedLotteryPayments();
    }

    public List<PaymentViewBean> findPaymentsForLastMonth() {
        DateTime oneMonthAgo = DateTime.now().minusMonths(1);
        return paymentRepository.findAllPaymentsAfter(oneMonthAgo.toDate());
    }

    public List<PaymentViewBean> findAllMatchedLotteryPayments() {
        return paymentRepository.findMatchedLotteryPayments();
    }

    public List<PaymentViewBean> findAllNonLotteryPayments() {
        return paymentRepository.findAllNonLotteryPayments();
    }

    public Payment savePayment(Payment payment) {
        reactivatePayerMembership(payment);
        forceDrawEligibilityRefresh();
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
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVParser parser = CSVParser.parse(contents, format);
        String paymentDateText;
        Date date;
        String accountNumber;
        String transactionType;
        String creditAmountText;
        String description;
        String name;
        Float creditAmount;
        for (CSVRecord record : parser) {
            paymentDateText = record.get(0).trim();
            date = DateTime.parse(paymentDateText, fmt).toDate();
            accountNumber = record.get(2).trim();
            transactionType = record.get(4).trim();
            creditAmountText = record.get(7).trim();
            description = record.get(8).trim();
            name="";

            if (commaCount(description) == 2) {
                String[] descArray = toArray(description);
                name = descArray[1].trim();
            }

            if (transactionType.equalsIgnoreCase("CR") && (!creditAmountText.isEmpty())) {
                creditAmount = Float.parseFloat(creditAmountText);
                Payment payment = new Payment(date, creditAmount, description, accountNumber, name, true);
                matchWithMember(payment);
                payments.add(payment);
            }
        }
        return payments;
    }

    private String[] toArray(String s) {
        String[] arrOfStr = s.split(",");
        return arrOfStr;
    }

    private int commaCount(String s){
        int commaCount = 0;
        for(int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') {
                commaCount++;
            }
        }
        return  commaCount;
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
                break;
            }
        }
    }

    private boolean tryMatchByMembershipNumberAndName(Payment payment) {
        boolean isMatched = false;
        String reference = payment.getCreditReference();
        String name = payment.getName();
        String fullDescription = (reference + name).toUpperCase();
        List<Member> allMembers = memberRepository.findAll();
        for (Member member : allMembers) {
            if (reference.equals("Giro " + member.getMembershipNumber()) ||
                    member.getMembershipNumber().equals(findMembershipNumberInReference(reference)) &&
                    (fullDescription.contains(member.getSurname().toUpperCase()) || fullDescription.contains(member.getForename().toUpperCase()))) {
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
        forceDrawEligibilityRefresh();
        return payments;
    }

    public Payment findById(Long id) {
        return paymentRepository.findOne(id);
    }

    public Payment updatePayment(Payment payment) {
        reactivatePayerMembership(payment);
        forceDrawEligibilityRefresh();
        return paymentRepository.save(payment);
    }

    public List<Payment> findPaymentsForMember(Member member) {
        return paymentRepository.findByMember(member);
    }

    public Payment findLatestLotteryPayment(Member member) {
        return paymentRepository.findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(member, true);
    }

    public List<Member> findPossiblePayers(Payment payment) {
        List<Member> possiblePayers = new ArrayList<>();
        String creditReference = payment.getCreditReference();
        String paymentName = payment.getName();
        String name = "";
        if (payment.getName() != null) {
            name = paymentName.toUpperCase().replace("MR ", " ").replace("MRS ", " ").replace("MISS ", " ").replace("DR ", " ");
        }
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
        if (null != member && (member.getStatus().equals("Closed") || member.getStatus().equals("TBC"))) {
            member.setStatus("Open");
            memberRepository.save(member);
        }
    }

    public void markPaymentAsNonLottery(Long id) {
        Payment payment = paymentRepository.getOne(id);
        payment.setLotteryPayment(false);
        forceDrawEligibilityRefresh();
        paymentRepository.save(payment);
    }

    public void markPaymentForLottery(Long id) {
        Payment payment = paymentRepository.getOne(id);
        payment.setLotteryPayment(true);
        forceDrawEligibilityRefresh();
        paymentRepository.save(payment);
    }
}
