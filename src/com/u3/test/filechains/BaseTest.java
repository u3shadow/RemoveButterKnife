package com.u3.test.filechains;

import org.junit.Before;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class BaseTest {
   protected Map<String,String> nameAndIdMap;
   protected Map<Integer,String> typeAndNameMap;
   protected String[] currentDoc;
   protected List<Integer> deleteLineNumbers;
   @Before
   public void init(){
       nameAndIdMap = new LinkedHashMap<>();
       typeAndNameMap = new LinkedHashMap<>();
       deleteLineNumbers = new ArrayList<>();
       currentDoc = new String[10];
   }
}
