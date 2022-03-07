package com.meishe.engine.util;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.common.utils.Logger;
import com.meishe.engine.bean.CommonData;
import com.meishe.engine.data.CutData;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class StoryboardUtil {
    public static final String STORYBOARD_KEY_ROTATION_Z = "rotationZ";
    public static final String STORYBOARD_KEY_SCALE_X = "scaleX";
    public static final String STORYBOARD_KEY_SCALE_Y = "scaleY";
    public static final String STORYBOARD_KEY_TRANS_X = "transX";
    public static final String STORYBOARD_KEY_TRANS_Y = "transY";
    private static final String TAG = "StoryboardUtil";

    public static String getImageBackgroundStory(String str, int i, int i2, Map<String, Float> map) {
        int i3 = i < i2 ? i2 : i;
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<storyboard sceneWidth=\"" + i + "\" sceneHeight=\"" + i2 + "\">\t\n<track source=\"" + str + "\" width=\"" + i3 + "\" height=\"" + i3 + "\" clipStart=\"0\" clipDuration=\"1\" repeat=\"true\">\n</track>\n<track source=\":1\" clipStart=\"0\" clipDuration=\"1\" repeat=\"true\">\n<effect name=\"transform\">\n<param name=\"scaleX\" value=\"" + map.get(STORYBOARD_KEY_SCALE_X) + "\"/>\n<param name=\"scaleY\" value=\"" + map.get(STORYBOARD_KEY_SCALE_Y) + "\"/>\n<param name=\"rotationZ\" value=\"" + map.get(STORYBOARD_KEY_ROTATION_Z) + "\"/>\n<param name=\"transX\" value=\"" + map.get(STORYBOARD_KEY_TRANS_X) + "\"/>\n<param name=\"transY\" value=\"" + map.get(STORYBOARD_KEY_TRANS_Y) + "\"/>\n</effect>\n</track>\n</storyboard>";
    }

    public static String getBlurBackgroundStory(int i, int i2, String str, float f, Map<String, Float> map) {
        NvsSize videoStreamDimension = NvsStreamingContext.getInstance().getAVFileInfo(str).getVideoStreamDimension(0);
        Map<String, Float> blurTransData = getBlurTransData(i, i2, videoStreamDimension.width, videoStreamDimension.height);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<storyboard sceneWidth=\"" + i + "\" sceneHeight=\"" + i2 + "\">\t\n<track source=\":1\" clipStart=\"0\" clipDuration=\"1\" repeat=\"true\">\n<effect name=\"fastBlur\">\n<param name=\"radius\" value=\"" + f + "\"/>\n</effect>\n<effect name=\"transform\">\n<param name=\"scaleX\" value=\"" + blurTransData.get(STORYBOARD_KEY_SCALE_X) + "\"/>\n<param name=\"scaleY\" value=\"" + blurTransData.get(STORYBOARD_KEY_SCALE_Y) + "\"/>\n<param name=\"rotationZ\" value=\"" + blurTransData.get(STORYBOARD_KEY_ROTATION_Z) + "\"/>\n<param name=\"transX\" value=\"" + blurTransData.get(STORYBOARD_KEY_TRANS_X) + "\"/>\n<param name=\"transY\" value=\"" + blurTransData.get(STORYBOARD_KEY_TRANS_Y) + "\"/>\n</effect>\n</track>\n<track source=\":1\" clipStart=\"0\" clipDuration=\"1\" repeat=\"true\">\n<effect name=\"transform\">\n<param name=\"scaleX\" value=\"" + map.get(STORYBOARD_KEY_SCALE_X) + "\"/>\n<param name=\"scaleY\" value=\"" + map.get(STORYBOARD_KEY_SCALE_Y) + "\"/>\n<param name=\"rotationZ\" value=\"" + map.get(STORYBOARD_KEY_ROTATION_Z) + "\"/>\n<param name=\"transX\" value=\"" + map.get(STORYBOARD_KEY_TRANS_X) + "\"/>\n<param name=\"transY\" value=\"" + map.get(STORYBOARD_KEY_TRANS_Y) + "\"/>\n</effect>\n</track>\n</storyboard>";
    }

    public static String getCropperStory(int i, int i2, float[] fArr) {
        if (fArr == null || fArr.length < 8) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < fArr.length; i3++) {
            sb.append(fArr[i3]);
            if (i3 < fArr.length - 1) {
                sb.append(",");
            }
        }
        String sb2 = sb.toString();
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<storyboard sceneWidth=\"" + i + "\" sceneHeight=\"" + i2 + "\">\n    <track source=\":1\" clipStart=\"0\" clipDuration=\"1\" repeat=\"true\">\n        <effect name=\"maskGenerator\">\n            <param name=\"keepRGB\" value=\"true\"/>\n            <param name=\"featherWidth\" value=\"0\"/>\n            <param name=\"region\" value=\"" + sb2 + "\"/>\n        </effect>\n    </track>\n</storyboard>";
    }

    public static String getTransform2DStory(int i, int i2, Map<String, Float> map) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<storyboard sceneWidth=\"" + i + "\" sceneHeight=\"" + i2 + "\">\t\n<track source=\":1\" clipStart=\"0\" clipDuration=\"1\" repeat=\"true\">\n<effect name=\"transform\">\n<param name=\"scaleX\" value=\"" + map.get(STORYBOARD_KEY_SCALE_X) + "\"/>\n<param name=\"scaleY\" value=\"" + map.get(STORYBOARD_KEY_SCALE_Y) + "\"/>\n<param name=\"rotationZ\" value=\"" + (-map.get(STORYBOARD_KEY_ROTATION_Z).floatValue()) + "\"/>\n<param name=\"transX\" value=\"" + map.get(STORYBOARD_KEY_TRANS_X) + "\"/>\n<param name=\"transY\" value=\"" + (-map.get(STORYBOARD_KEY_TRANS_Y).floatValue()) + "\"/>\n</effect>\n</track>\n</storyboard>";
    }

    private static Map<String, Float> getBlurTransData(int i, int i2, int i3, int i4) {
        HashMap hashMap = new HashMap();
        Float valueOf = Float.valueOf(1.0f);
        int i5 = (((((float) i3) * 1.0f) / ((float) i4)) > ((((float) i) * 1.0f) / ((float) i2)) ? 1 : (((((float) i3) * 1.0f) / ((float) i4)) == ((((float) i) * 1.0f) / ((float) i2)) ? 0 : -1));
        hashMap.put(STORYBOARD_KEY_SCALE_X, valueOf);
        hashMap.put(STORYBOARD_KEY_SCALE_Y, valueOf);
        hashMap.put(STORYBOARD_KEY_ROTATION_Z, Float.valueOf(0.0f));
        hashMap.put(STORYBOARD_KEY_TRANS_X, Float.valueOf(0.0f));
        hashMap.put(STORYBOARD_KEY_TRANS_Y, Float.valueOf(0.0f));
        return hashMap;
    }

    public static float getBlurStrengthFromStory(String str) {
        Document document = getDocument(str);
        if (document == null) {
            return -1.0f;
        }
        NodeList elementsByTagName = document.getElementsByTagName("param");
        if (elementsByTagName.getLength() == 0) {
            return -1.0f;
        }
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            NamedNodeMap attributes = elementsByTagName.item(i).getAttributes();
            if (!(attributes == null || attributes.getNamedItem("name") == null || !"radius".equals(attributes.getNamedItem("name").getNodeValue()))) {
                return Float.parseFloat(attributes.getNamedItem("value").getNodeValue());
            }
        }
        return -1.0f;
    }

    public static String getSourcePathFromStory(String str) {
        Document document = getDocument(str);
        if (document == null) {
            return null;
        }
        NodeList elementsByTagName = document.getElementsByTagName("track");
        if (elementsByTagName.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            Node namedItem = elementsByTagName.item(i).getAttributes().getNamedItem(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME);
            if (!(namedItem == null || ":1".equals(namedItem.getNodeValue()))) {
                return namedItem.getNodeValue();
            }
        }
        return null;
    }

    public static CutData parseStoryToCatData(String str, String str2) {
        Document document = getDocument(str2);
        if (document == null) {
            return null;
        }
        CutData cutData = new CutData();
        NodeList elementsByTagName = document.getElementsByTagName("param");
        if (elementsByTagName.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            NamedNodeMap attributes = elementsByTagName.item(i).getAttributes();
            if (!(attributes == null || attributes.getNamedItem("name") == null)) {
                if (STORYBOARD_KEY_ROTATION_Z.equals(attributes.getNamedItem("name").getNodeValue())) {
                    cutData.putTransformData(attributes.getNamedItem("name").getNodeValue(), -Float.parseFloat(attributes.getNamedItem("value").getNodeValue()));
                } else if (STORYBOARD_KEY_TRANS_Y.equals(attributes.getNamedItem("name").getNodeValue())) {
                    cutData.putTransformData(attributes.getNamedItem("name").getNodeValue(), -Float.parseFloat(attributes.getNamedItem("value").getNodeValue()));
                } else {
                    cutData.putTransformData(attributes.getNamedItem("name").getNodeValue(), Float.parseFloat(attributes.getNamedItem("value").getNodeValue()));
                }
            }
        }
        Document document2 = getDocument(str);
        if (document2 == null) {
            return cutData;
        }
        NamedNodeMap attributes2 = document2.getElementsByTagName("storyboard").item(0).getAttributes();
        int parseInt = Integer.parseInt(attributes2.getNamedItem("sceneWidth").getNodeValue());
        int parseInt2 = Integer.parseInt(attributes2.getNamedItem("sceneHeight").getNodeValue());
        NodeList elementsByTagName2 = document2.getElementsByTagName("param");
        if (elementsByTagName2.getLength() > 0) {
            for (int i2 = 0; i2 < elementsByTagName2.getLength(); i2++) {
                NamedNodeMap attributes3 = elementsByTagName2.item(i2).getAttributes();
                if (attributes3 != null) {
                    Log.e(TAG, "parseStoryToCatData: value = " + attributes3.getNamedItem("name").getNodeValue());
                    if (attributes3.getNamedItem("name") != null && "region".equals(attributes3.getNamedItem("name").getNodeValue())) {
                        String nodeValue = attributes3.getNamedItem("value").getNodeValue();
                        cutData.setRatio(getRationFromRegion(nodeValue, parseInt, parseInt2));
                        cutData.setRatioValue(getRatioValueFromRegion(nodeValue, parseInt, parseInt2));
                    }
                }
            }
        }
        return cutData;
    }

    private static int getRationFromRegion(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        String[] split = str.split(",");
        if (split.length != 8) {
            return 0;
        }
        float parseFloat = Float.parseFloat(split[3]) - Float.parseFloat(split[5]);
        return CommonData.AspectRatio.getAspect((((float) i) * (Float.parseFloat(split[2]) - Float.parseFloat(split[0]))) / (((float) i2) * parseFloat));
    }

    private static float getRatioValueFromRegion(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return 0.0f;
        }
        String[] split = str.split(",");
        float parseFloat = Float.parseFloat(split[3]) - Float.parseFloat(split[5]);
        return (((float) i) * (Float.parseFloat(split[2]) - Float.parseFloat(split[0]))) / (((float) i2) * parseFloat);
    }

    private static Document getDocument(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(str)));
        } catch (Exception e) {
            Logger.e(TAG, "getDocument error:" + e.getMessage());
            return null;
        }
    }
}
