package com.u3.filechains;

import com.u3.codegenerator.FindViewByIdWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratCodeChain extends BaseChain {
    private List code;

    public GeneratCodeChain(List code){
        this.code =  code;
    }
    @Override
    public void process() {
        for (Map.Entry<String,String> entry:nameAndIdMap.entrySet()){
            String codes;
            codes = entry.getKey() + "findViewById("+entry.getValue()+");";
            code.add(codes);
        }
    }
}
