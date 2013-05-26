
package gpfinance.datatypes;

import java.util.Random;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public enum Fund implements Indicator {

    GrossMargin09(0, "Gross Margin % 2009"),
    GrossMargin10(1, "Gross Margin % 2010"),
    GrossMarginChange(2, "Gross Margin % YoY % change"),
    OperatingMargin09(3, "Operating Margin % 2009"),
    OperatingMargin10(4, "Operating Margin % 2010"),
    OperatingMarginChange(5, "Operating Margin % YoY % change"),
    EarningsPerShare09(6, "Earnings Per Share USD 2009"),
    EarningsPerShare10(7, "Earnings Per Share USD 2010"),
    EarningsPerShareChange(8, "Earnings Per Share USD YoY % change"),
    BookValuePerShare09(9, "Book Value Per Share USD 2009"),
    BookValuePerShare10(10, "Book Value Per Share USD 2009"),
    BookValuePerShareChange(11, "Book Value Per Share USD 2009"),
    SGAExpenses09(12, "SG&A 2009"),
    SGAExpenses10(13, "SG&A 2010"),
    SGAExpensesChange(14, "SG&A YoY % change"),
    ResearchDevelopment09(15, "R&D 2009"),
    ResearchDevelopment10(16, "R&D 2010"),
    ResearchDevelopmentChange(17, "R&D YoY % change"),
    ReturnOnAssets09(18, "Return on Assets % 2009"),
    ReturnOnAssets10(19, "Return on Assets % 2010"),
    ReturnOnAssetsChange(20, "Return on Assets % YoY % change"),
    ReturnOnEquity09(21, "Return on Equity % 2009"),
    ReturnOnEquity10(22, "Return on Equity % 2010"),
    ReturnOnEquityChange(23, "Return on Equity % YoY % change"),
    ReturnOnInvestedCapital09(24, "Return on Invested Capital % 2009"),
    ReturnOnInvestedCapital10(25, "Return on Invested Capital % 2010"),
    ReturnOnInvestedCapitalChange(26, "Return on Invested Capital % YoY % change"),
    CapExSales09(27, "Cap Ex as a % of Sales 2009"),
    CapExSales10(28, "Cap Ex as a % of Sales 2010"),
    CapExSalesChange(29, "Cap Ex as a % of Sales YoY % change"),
    TotalCurrentAssets09(30, "Total Current Assets 2009"),
    TotalCurrentAssets10(31, "Total Current Assets 2010"),
    TotalCurrentAssetsChange(32, "Total Current Assets YoY % change"),
    TotalCurrentLiabilities09(33, "Total Current Liabilities 2009"),
    TotalCurrentLiabilities10(34, "Total Current Liabilities 2010"),
    TotalCurrentLiabilitiesChange(35, "Total Current Liabilities YoY % change"),
    CurrentRatio09(36, "Current Ratio 2009"),
    CurrentRatio10(37, "Current Ratio 2010"),
    CurrentRatioChange(38, "Current Ratio YoY % change"),
    QuickRatio09(39, "Quick Ratio 2009"),
    QuickRatio10(40, "Quick Ratio 2010"),
    QuickRatioChange(41, "Quick Ratio YoY % change");

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
