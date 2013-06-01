#!/usr/bin/env ruby

# Spits out lists of handy excel functions

fundamentals = '    GrossMargin09(0, "Gross Margin % 2009"),
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
    QuickRatioChange(41, "Quick Ratio YoY % change");'

technicals = '1 Day % price movement
7 Day % price movement
14 Day % price movement
30 Day % price movement
60 Day % price movement
90 Day % price movement
7 Day Simple Moving Average
14 Day Simple Moving Average
30 Day Simple Moving Average
60 Day Simple Moving Average
90 Day Simple Moving Average
180 Day Simple Moving Average
7 Day Volume Price Trend
14 Day Volume Price Trend
30 Day Volume Price Trend
60 Day Volume Price Trend
90 Day Volume Price Trend
180 Day Volume Price Trend
7 Day Momentum
14 Day Momentum
30 Day Momentum
60 Day Momentum
90 Day Momentum
7 Day % Rate of change
14 Day % Rate of change
30 Day % Rate of change
60 Day % Rate of change
90 Day % Rate of change
Aroon up 25
Aroon down 25
1 day force index
7 day force index
14 day force index
30 day force index
60 day force index
90 day force index
7 Day True Range
14 Day True Range
30 Day True Range
60 Day True Range
90 Day True Range
180 Day True Range'

def parse_fund(fundamentals)
	temp = fundamentals.split("\n")
	temp.each_with_index do |token, index|
		pair = token.split("(")
		puts pair[0].gsub!("    ", "")
	end
end

def parse_tech(technicals)
	temp = technicals.split("\n")
	temp.each_with_index do |token, index|
		puts token
	end
end


def countif(lines, column_char)
# Prints countif function given column letter
	lines.times do |index|
		puts "=countif(#{column_char}3:#{column_char}403,#{index})"
	end
end

###############################################################################
# Main
###############################################################################

# print indicator labels
puts "LABELS"
puts "******"
parse_fund(fundamentals)
puts "###############################################################################"
parse_tech(technicals)
puts "###############################################################################"

# print function lists for indicators countifs
puts "FUNCTIONS"
puts "*********"
countif(42,'F')
puts "###############################################################################"
countif(42,'L')
puts "###############################################################################"
countif(42,'R')
puts "###############################################################################"
countif(42,'X')