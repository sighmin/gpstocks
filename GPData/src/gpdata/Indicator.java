package gpdata;


class Indicator {

    String indicatorName;
    String[] values;

    Indicator(String name, int valuesSize) {
        indicatorName = name;
        values = new String[valuesSize];
    }
}