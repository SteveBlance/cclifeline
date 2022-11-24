set @member_number = 8073;

delete from addresses 
where MEMBER_ID = (select id from members
                   where membership_number = @member_number);

delete from payment_references
where MEMBER_ID = (select id from members
                   where membership_number = @member_number);

delete from payments
where MEMBER_ID = (select id from members
                   where membership_number = @member_number);

delete from prizes
where winner_id = (select id from members
                   where membership_number = @member_number);								

delete from members
where membership_number = @member_number;