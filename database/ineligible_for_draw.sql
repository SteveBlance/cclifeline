select m.MEMBERSHIP_NUMBER, m.FORENAME, m.SURNAME, m.email, m.LANDLINE_NUMBER, m.MOBILE_NUMBER, m.PAYER_TYPE, m.JOIN_DATE, p.PAYMENT_AMOUNT, max(p.PAYMENT_DATE) LAST_PAYMENT from members m, payments p
where status = 'Open'
and IS_ELIGIBLE_FOR_DRAW = false
and p.member_id = m.id
group by m.MEMBERSHIP_NUMBER, m.FORENAME, m.SURNAME, m.email, m.LANDLINE_NUMBER, m.MOBILE_NUMBER, m.PAYER_TYPE, m.JOIN_DATE, p.PAYMENT_AMOUNT
order by max(p.PAYMENT_DATE) desc;
