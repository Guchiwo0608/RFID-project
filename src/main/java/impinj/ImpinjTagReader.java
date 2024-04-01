package impinj;

import com.impinj.octane.*;
import java.util.Scanner;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

public class ImpinjTagReader {

    public static void readTagsPeriodicTrigger(@Required String hostname, short[] antennaPortNumbers) {

        try {

            if (hostname == null) {
                throw new Exception("Must specify the '"
                        + Properties.hostname + "' property");
            }

            if (antennaPortNumbers == null) {
                antennaPortNumbers = new short[] {};
            }

            ImpinjReader reader = new ImpinjReader();
            System.out.println("Connecting");
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeDopplerFrequency(true);
            report.setIncludePeakRssi(true);
            report.setIncludePhaseAngle(true);
            report.setIncludeChannel(true);
            report.setIncludePcBits(true);
            report.setIncludeCrc(true);
            report.setIncludeGpsCoordinates(true);
            report.setIncludeFastId(true);
            report.setIncludeAntennaPortNumber(true);
            report.setIncludeLastSeenTime(true);
            report.setIncludeFirstSeenTime(true);
            report.setIncludeSeenCount(true);
            report.setMode(ReportMode.BatchAfterStop);

            // settings.setRfMode(1002);
            // settings.setSearchMode(SearchMode.DualTarget);
            // settings.setSession(2);

            settings.getAutoStart().setMode(AutoStartMode.Periodic);
            settings.getAutoStart().setPeriodInMs(1000);
            settings.getAutoStop().setMode(AutoStopMode.Duration);
            settings.getAutoStop().setDurationInMs(500);

            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(antennaPortNumbers);

            antennas.setIsMaxRxSensitivity(true);
            antennas.setIsMaxTxPower(true);

            reader.setTagReportListener(new TagReportListenerImplementation());

            reader.applySettings(settings);

            try (Scanner s = new Scanner(System.in)) {
                s.nextLine();
            }
            reader.stop();
            reader.disconnect();
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public static void readTagsPeriodicTrigger(@Required String hostname) {
        ImpinjTagReader.readTagsPeriodicTrigger(hostname, null);
    }
}