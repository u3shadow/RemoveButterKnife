package com.u3.filechains;

import java.util.List;
import java.util.Map;

public abstract class BaseChain {
   protected BaseChain next;
   protected String[] currentDoc;
   protected List deleteLineNumbers;
   protected Map<String,String> nameAndIdMap;
   protected Map<Integer,String> typeAndNameMap;
   public void setNext(BaseChain next){
      this.next = next;
   }
    final public void handle(String[] currentDoc,List deleteLineNumbers,Map nameAndIdMap,Map typeAndNameMap){
        this.deleteLineNumbers = deleteLineNumbers;
        this.nameAndIdMap = nameAndIdMap;
        this.typeAndNameMap = typeAndNameMap;
        this.currentDoc = currentDoc;
        process();
        dispatcher();
    }
    abstract public void process();
    private void dispatcher(){
        if(next != null) {
            next.handle(currentDoc, deleteLineNumbers, nameAndIdMap, typeAndNameMap);
        }
    }
}
