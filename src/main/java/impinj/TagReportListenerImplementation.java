package impinj;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class TagReportListenerImplementation implements TagReportListener {

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {

        List<JSONObject> tagsData = new ArrayList<>();

        for (Tag t : report.getTags()) {

            Map<String, String> data = new HashMap<String, String>();
            data.put("EPC", t.getEpc().toString());

            data.put("antenna_number",
                    t.isAntennaPortNumberPresent() ? String.valueOf(t.getAntennaPortNumber()) : null);
            data.put("first_seen_time", t.isFirstSeenTimePresent() ? t.getFirstSeenTime().toString() : null);
            data.put("last_seen_time", t.isLastSeenTimePresent() ? t.getLastSeenTime().toString() : null);
            data.put("seen_count", t.isSeenCountPresent() ? String.valueOf(t.getTagSeenCount()) : null);
            data.put("doppler_frequency",
                    t.isRfDopplerFrequencyPresent() ? String.valueOf(t.getRfDopplerFrequency()) : null);
            data.put("rssi", t.isPeakRssiInDbmPresent() ? String.valueOf(t.getPeakRssiInDbm()) : null);
            data.put("channel", t.isChannelInMhzPresent() ? String.valueOf(t.getChannelInMhz()) : null);
            data.put("pc_bits", t.isPcBitsPresent() ? String.valueOf(t.getPcBits()) : null);
            data.put("gps_coordinates", t.isGpsCoordinatesPresent() ? t.getGpsCoodinates().toString() : null);
            data.put("crc", t.isCrcPresent() ? String.valueOf(t.getCrc()) : null);
            data.put("fast_id", t.isFastIdPresent() ? t.getTid().toString() : null);
            data.put("model", t.isFastIdPresent() ? t.getModelDetails().getModelName().toString() : null);
            data.put("epcsize", t.isFastIdPresent() ? String.valueOf(t.getModelDetails().getEpcSizeBits()) : null);
            data.put("usermemsize",
                    t.isFastIdPresent() ? String.valueOf(t.getModelDetails().getUserMemorySizeBits()) : null);
            data.put("supports_qt", t.isFastIdPresent() ? String.valueOf(t.getModelDetails().isSupportsQt()) : null);

            tagsData.add(new JSONObject(data));

            System.out.println(data.toString());

        }
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/java/com/temp_dir/temp.json"))) {
            out.write(tagsData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
