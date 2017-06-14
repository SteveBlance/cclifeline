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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PaymentService {

    private static final String TRANSACTION_DATE = "Transaction Date";
    private static final String TRANSACTION_TYPE = "Transaction Type";
    private static final String SORT_CODE = "Sort Code";
    private static final String ACCOUNT_NUMBER = "Account Number";
    private static final String DESCRIPTION = "Transaction Description";
    private static final String CREDIT_AMOUNT = "Credit Amount";
    private static final String DEBIT_AMOUNT = "Debit Amount";
    private static final String BALANCE = "Balance";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentReferenceRepository paymentReferenceRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public PaymentReference savePaymentReference(PaymentReference paymentReference) {
        return paymentReferenceRepository.save(paymentReference);

    }

    public List<Payment> parsePayments(String contents) throws IOException, NumberFormatException {
        List<Payment> payments = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/mm/yyyy");
        String preppedContents = contents.replace("Balance,", "Balance");
        CSVFormat format = CSVFormat.DEFAULT.withHeader(TRANSACTION_DATE, TRANSACTION_TYPE, SORT_CODE, ACCOUNT_NUMBER, DESCRIPTION, DEBIT_AMOUNT, CREDIT_AMOUNT, BALANCE);
        CSVParser parser = CSVParser.parse(preppedContents, format);

        for (CSVRecord record : parser) {
            String paymentDateText = record.get(TRANSACTION_DATE);
            if (paymentDateText.equals(TRANSACTION_DATE)) {
                continue;
            }
            Date date = DateTime.parse(paymentDateText, fmt).toDate();
            String transactionType = record.get(TRANSACTION_TYPE);
            String sortCode = record.get(SORT_CODE);
            String accountNumber = record.get(ACCOUNT_NUMBER);
            String description = record.get(DESCRIPTION);
            String creditAmountText = record.get(CREDIT_AMOUNT);
            if (transactionType.equalsIgnoreCase("FPI") && (!creditAmountText.isEmpty())) {
                Float creditAmount = Float.parseFloat(creditAmountText);
                Payment payment = new Payment(date, creditAmount, description, sortCode + " " + accountNumber);
                matchWithMember(payment);
                payments.add(payment);
            }
        }
        return payments;
    }

    private void matchWithMember(Payment payment) {
        boolean paymentMatched = tryMatchByMembershipNumber(payment);
        if (!paymentMatched) {
            tryMatchByFullPaymentReference(payment);
        }

    }

    private void tryMatchByFullPaymentReference(Payment payment) {
        String reference = payment.getCreditReference();

        List<PaymentReference> allReferences = paymentReferenceRepository.findAll();
        for (PaymentReference paymentReference : allReferences) {
            if (reference.equalsIgnoreCase(paymentReference.getReference())) {
                payment.setMember(paymentReference.getMember());
            }
        }
    }

    private boolean tryMatchByMembershipNumber(Payment payment) {
        boolean isMatched = false;
        String reference = payment.getCreditReference();
        //TODO: change to findAll Open Members
        List<Member> allMembers = memberRepository.findAll();
        for (Member member : allMembers) {
            if (member.getMembershipNumber().equals(findMembershipNumberInReference(reference))) {
                payment.setMember(member);
                isMatched = true;
                break;
            }
        }
        return isMatched;
    }

    private Long findMembershipNumberInReference(String paymentReference) {
        Pattern pattern = Pattern.compile("(\\s\\d{4}\\s)");
        Matcher matcher = pattern.matcher(paymentReference);
        String membershipNumberText = "";
        if (matcher.find()) {
            membershipNumberText = matcher.group(1).trim();
        }
        return membershipNumberText.isEmpty() ? 0L : Long.parseLong(membershipNumberText);
    }
}
