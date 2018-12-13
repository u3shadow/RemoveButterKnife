package com.u3.test.filechains;

import com.u3.filechains.BaseChain;
import com.u3.filechains.FindAPIUseChain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FindAPIUseChainTest extends BaseTest{
    BaseChain chain;
    @Before
    public void initChain(){
        chain = new FindAPIUseChain();
    }
    @Test
    public void test_with_api_use() {
        currentDoc[0] = "NotUseApi();";
        currentDoc[1] = "ButterKnife.useApi();";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap,typeAndNameMap);
        int expect = 1;
        int result = deleteLineNumbers.size();
        assertEquals(expect,result);
    }
    @Test
    public void test_without_api_use() {
        currentDoc[0] = "NotUseApi();";
        currentDoc[1] = "";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap,typeAndNameMap);
        int expect = 0;
        int result = deleteLineNumbers.size();
        assertEquals(expect,result);
    }

}