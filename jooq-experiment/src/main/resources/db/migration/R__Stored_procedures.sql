drop procedure if exists AddNumbers;

delimiter $$

create procedure AddNumbers(in a int, in b int)
begin
    select (a + b) aAndB;
end

$$

delimiter ;
