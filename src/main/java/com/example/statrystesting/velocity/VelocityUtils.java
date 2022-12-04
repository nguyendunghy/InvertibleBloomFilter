package com.example.statrystesting.velocity;

import com.example.statrystesting.ibf.OneHashingBloomFilterUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class VelocityUtils {


    public static String getInvertibleBloomFilterTemplate(String templateFilename, long[] divisors,
                                                           String macroName, Map<String, Object> templateParams) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.putAll(templateParams);
        hashMap.put("cellsCount", OneHashingBloomFilterUtils.totalCellCount(divisors));
        hashMap.put("primeDivisors", divisors);
        hashMap.put("partitionOffsets", OneHashingBloomFilterUtils.partitionOffsets(divisors));
        hashMap.put("moduloDivisor",11);//TODO: understand this variable
        hashMap.put("table","ibf_data");
        hashMap.put("useLegacyRowHash",true);
        hashMap.put("keyLength",3);
        hashMap.put("keyCount",3);
        hashMap.put("output", "#" + macroName + "()");

        return generate(templateFilename, hashMap);
    }


    public static String generate(String vmFilePath, HashMap<String, Object> hashMap) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template t = velocityEngine.getTemplate(vmFilePath);
        VelocityContext context = new VelocityContext();
        for (String key : hashMap.keySet()) {
            context.put(key, hashMap.get(key));
        }
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }
}
