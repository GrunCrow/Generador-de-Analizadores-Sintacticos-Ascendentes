package generated;

import grammar_parser.*;

 public class Parser extends SLRParser implements TokenConstants, SymbolConstants {

    public Parser() throws SintaxException, IOException {
        initRules();
        initActionTable();
        initGotoTable();
    }

     private void initRules() {
     		int[][] initRule = {
{ 0, 0 } ,
{  ,  },{  ,  },{  ,  },{ ,  },{ ,  },{ ,  },{ ,  },{ ,  },{ ,  },{ ,  },{ ,   },{ ,  },{ ,  }          };
    		this.rule = initRule;;
    }

     private void initActionTable() {
    }

       private void initGotoTable() {
    }

}
