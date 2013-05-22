
package gpfinance.datatypes;

import java.util.Random;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public enum Fund implements Indicator {

    GM(0, "Gross Margin %"),
    OM(1, "Operating Margin %"),
    EPS(2, "Earnings Per Share USD"),
    BVPS(3, "Book Value Per Share USD"),
    SGA(4, "SG&A"),
    RD(5, "R&D"),
    ROA(6, "Return on Assets %"),
    ROE(7, "Return on Equity %"),
    ROIC(8, "Return on Invested Capital %"),
    CE(9, "Cap Ex as a % of Sales"),
    CA(10, "Total Current Assets"),
    CL(11, "Total Current Liabilities"),
    CR(12, "Current Ratio"),
    QR(13, "Quick Ratio");

    private int code;
    private String label;

    private Fund(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public static Fund getRandom() {
        Fund[] indicators = Fund.values();
        return indicators[new Random().nextInt(indicators.length)];
    }
}
