select distinct email as email_address, FORENAME as first_name, SURNAME as last_name from (
select m.email, m.FORENAME, m.SURNAME
from members m
where email is not null
and email like '%@%'
and m.deceased = false
and email_opt_out = false
and STATUS = 'Open'
union
select m.email, m.FORENAME, m.SURNAME
from members m
where JOIN_DATE > (DATE_SUB(curdate(), INTERVAL 60 DAY))
) s
order by 3, 2;
