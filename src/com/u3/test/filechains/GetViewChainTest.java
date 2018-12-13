package com.u3.test.filechains;

import com.u3.filechains.BaseChain;
import com.u3.filechains.FindImportChain;
import com.u3.filechains.GetViewStatementMapChain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetViewChainTest extends BaseTest{
    BaseChain chain;
    @Before
    public void initChain(){
        chain = new GetViewStatementMapChain();
    }
    @Test
    public void test_with_bind_use() {
        currentDoc = new String[2];
        currentDoc[0] = "@BindView(R.id.test)";
        currentDoc[1] = "ImageView test;";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap,typeAndNameMap);
        int expect = 0;
        int result = typeAndNameMap.size();
        assertEquals(expect,result);
    }
    @Test
    public void test_without_bind_use() {
        currentDoc = new String[2];
        currentDoc[0] = "NotUseApi();";
        currentDoc[1] = "";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap,typeAndNameMap);
        int expect = 0;
        int result = typeAndNameMap.size();
        assertEquals(expect,result);
    }

}