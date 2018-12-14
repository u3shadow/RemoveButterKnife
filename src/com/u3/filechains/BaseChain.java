package com.u3.filechains;

import java.util.List;
import java.util.Map;

public abstract class BaseChain {
   protected BaseChain next;
   protected String[] currentDoc;
   protected List deleteLineNumbers;
   protected Map<String,String> nameAndIdMap;
   public void setNext(BaseChain next){
      this.next = next;
   }
    final public void handle(String[] currentDoc,List deleteLineNumbers,Map nameAndIdMap){
        this.deleteLineNumbers = deleteLineNumbers;
        this.nameAndIdMap = nameAndIdMap;
        this.currentDoc = currentDoc;
        process();
        dispatcher();
    }
    abstract public void process();
    private void dispatcher(){
        if(next != null) {
            next.handle(currentDoc, deleteLineNumbers, nameAndIdMap);
        }
    }
}
