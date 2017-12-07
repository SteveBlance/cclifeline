-- All registered members past and present
select count(1) from members;  -- 1439

-- Current members
select count(1) from members where status = 'Open';   -- 977

-- Current members eligible for draw
select count(1) from members where status = 'Open' and is_eligible_for_draw = 1;   -- 948

-- Current members ineligible for draw due to lapsed payments
select count(1) from members where status = 'Open' and is_eligible_for_draw = 0;  -- 29

-- Members ineligible for draw having not started payments
select count(1) from members where status = 'TBC';  -- 30

-- Current Legacy memberships
select count(1) from members  where status = 'Open' and MEMBERSHIP_TYPE = 'Legacy';  -- 149

-- Current Lifeline memberships
select count(1) from members  where status = 'Open' and MEMBERSHIP_TYPE = 'Lifeline'; -- 828

-- New members registered (change mask date to previus meeting)
select count(1) from members where JOIN_DATE between DATE_FORMAT(NOW() ,'%2017-%11-09') AND NOW();

