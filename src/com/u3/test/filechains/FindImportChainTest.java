package com.u3.test.filechains;

import com.u3.filechains.BaseChain;
import com.u3.filechains.FindBindAnnotationChain;
import com.u3.filechains.FindImportChain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindImportChainTest extends BaseTest{
    BaseChain chain;
    @Before
    public void initChain(){
        chain = new FindImportChain();
    }
    @Test
    public void test_with_bind_use() {
        currentDoc = new String[2];
        currentDoc[0] = "import butterknife.Bind;";
        currentDoc[1] = "";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 0;
        int result = deleteLineNumbers.get(0);
        assertEquals(expect,result);
    }
    @Test
    public void test_without_bind_use() {
        currentDoc = new String[2];
        currentDoc[0] = "NotUseApi();";
        currentDoc[1] = "";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 0;
        int result = deleteLineNumbers.size();
        assertEquals(expect,result);
    }

}