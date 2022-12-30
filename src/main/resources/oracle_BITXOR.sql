CREATE OR REPLACE FUNCTION FT_BITXOR(input NUMBER)
   RETURN NUMBER PARALLEL_ENABLE AGGREGATE
USING FT_BITXOR_IMPL;
/
CREATE OR REPLACE TYPE FT_BITXOR_IMPL AS OBJECT (
   accum  NUMBER,
   STATIC FUNCTION ODCIAggregateInitialize(sctx IN OUT FT_BITXOR_IMPL) return number,
   MEMBER FUNCTION ODCIAggregateIterate(self IN OUT FT_BITXOR_IMPL, value IN number) return number,
   MEMBER FUNCTION ODCIAggregateMerge(self IN OUT FT_BITXOR_IMPL, ctx2 IN FT_BITXOR_IMPL) return number,
   MEMBER FUNCTION ODCIAggregateTerminate(self IN FT_BITXOR_IMPL, ReturnValue OUT NUMBER, flags IN number) return number
);
/
create or replace type body FT_BITXOR_IMPL is
    static function ODCIAggregateInitialize(sctx IN OUT FT_BITXOR_IMPL) return number is
begin
        sctx := FT_BITXOR_IMPL(0);
return ODCIConst.Success;
end;

    member function ODCIAggregateIterate(self IN OUT FT_BITXOR_IMPL, value IN number) return number is
begin
        self.accum := ((self.accum + value) - (2 * BITAND(self.accum,value)));
return ODCIConst.Success;
end;

    member function ODCIAggregateMerge(self IN OUT FT_BITXOR_IMPL, ctx2 IN FT_BITXOR_IMPL) return number is
begin
        self.accum := ((self.accum + ctx2.accum) - (2 * BITAND(self.accum,ctx2.accum)));
return ODCIConst.Success;
end;

    member FUNCTION ODCIAggregateTerminate(self IN FT_BITXOR_IMPL, ReturnValue OUT NUMBER, flags IN number) return number is
begin
        ReturnValue := self.accum;
return ODCIConst.Success;
end;
END;


