#!/usr/bin/env ruby

# Script to extract averages from files, and write it to stdout - pipe output to a new file

filename = ARGV[0]
linenum = 0
averages = []
line_averages = []
line_averages_strarr = []

# Format: iter | f() | size | f()/size | heterogeneity | indicator name , indicator code

# Open file
File.open(filename, "r") do |fd|
	# Iterate each line
	fd.each_line do |line|
		linenum += 1
		if line.include?("#")
		else
			# Split line, remove first element (iteration ref), convert to float list, convert scientific to decimal notation
			line_averages_strarr = line.split("|")
			line_averages_strarr.shift
			line_averages = line_averages_strarr.collect { |s| s.to_f }
			averages[linenum] = line_averages.reduce(:+).to_f / line_averages.size.to_f
			averages[linenum] = ("%.500f" % averages[linenum]).sub(/\.?0*$/, "")
			puts averages[linenum]
		end
	end
end
