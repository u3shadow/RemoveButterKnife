package com.u3.filechains;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GetViewStatementMapChain extends BaseChain {
    @Override
    public void process() {
        String pattern = "@(BindView|InjectView|Bind)\\(R.id.*\\)*;";
        Pattern r = Pattern.compile(pattern);
        int j;
        for (j = 0; j < currentDoc.length; j++){
            Matcher m = r.matcher(currentDoc[j]);
            if (m.find( )) {
                String s2 = currentDoc[j].substring(currentDoc[j].indexOf(")")+1).trim();
                String name = s2.split(" ")[1];
                name = name.substring(0,name.length()-1);
                String type = s2.split(" ")[0];
                typeAndNameMap.put(j,type + " "+name);
            }
        }
    }
}
