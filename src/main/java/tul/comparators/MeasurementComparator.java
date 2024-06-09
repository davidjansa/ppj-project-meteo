package tul.comparators;

import tul.data.Measurement;

public class MeasurementComparator implements java.util.Comparator<Measurement> {
    @Override
    public int compare(Measurement o1, Measurement o2) {
        if (o1.getmMtime() == o2.getmMtime()) return 0;
        if (o1.getmMtime().after(o2.getmMtime())) return 1;
        return -1;
    }
}
