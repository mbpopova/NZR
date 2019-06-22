package mycompany.nzr.dp;

public enum Action {
   DDL_MISC(1), DDL_DROP_FK(2), DDL_DROP_PK(3), DDL_CREATE_PK(4), DDL_CREATE_FK(5),DDL_CREATE_TABLE(6),  
   DELETES(7), INSERTS(8), NONE(0), JUST_STARTED(9);
   
   private int code;
   
   private Action(int code) {
	   this.code = code;
   }
   
   public int getCode() {
	   return code;
   }
}
