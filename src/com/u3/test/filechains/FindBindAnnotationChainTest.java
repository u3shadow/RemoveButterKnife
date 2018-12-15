package com.u3.test.filechains;

import com.u3.filechains.BaseChain;
import com.u3.filechains.FindAPIUseChain;
import com.u3.filechains.FindBindAnnotationChain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindBindAnnotationChainTest extends BaseTest{
    BaseChain chain;
    @Before
    public void initChain(){
        chain = new FindBindAnnotationChain();
    }
    @Test
    public void test_with_bind_use() {
        currentDoc = new String[2];
        currentDoc[0] = "@BindView(R.id.test)";
        currentDoc[1] = "ImageView image";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 1;
        int result = nameAndIdMap.size();
        assertEquals(expect,result);
    }
     @Test
    public void test_with_bind_R2_use() {
        currentDoc = new String[2];
        currentDoc[0] = "@BindView(R2.id.test)";
        currentDoc[1] = "ImageView image";
        chain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
        int expect = 1;
        int result = nameAndIdMap.size();
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