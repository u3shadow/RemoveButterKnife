package com.u3.test.filechains;

import com.u3.filechains.BaseChain;
import com.u3.filechains.FindOnClickChain;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FindOnClickChainTest extends BaseTest{
    BaseChain chain;
    Map map;
    @Before
    public void initChain(){
        map = new LinkedHashMap<>();
        chain = new FindOnClickChain(map);
    }
    @Test
    public void test_can_find_onclick(){
        currentDoc = new String[3];
        currentDoc[0] = "other string";
        currentDoc[1] = "@OnClick(R.id.test)";
        currentDoc[2] = "public void click()";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 1;
        int result = deleteLineNumbers.get(0);
        assertEquals(expect,result);
    }
     @Test
    public void test_can_find_single_id(){
        currentDoc = new String[3];
        currentDoc[0] = "other string";
        currentDoc[1] = "@OnClick(R.id.test)";
        currentDoc[2] = "public void click()";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 1;
        int result = deleteLineNumbers.get(0);
        assertEquals(expect,result);
    }
      @Test
    public void test_can_find_multiple_id(){
        currentDoc = new String[3];
        currentDoc[0] = "other string";
        currentDoc[1] = "@OnClick({R.id.test,R2.id.test1,R.id.test})";
        currentDoc[2] = "public void click(Button button)";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 1;
        int result = map.keySet().size();
        assertEquals(expect,result);
    }

}