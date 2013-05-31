#!/usr/bin/env ruby

number_of_lines = 413
outputCSVfilename = "consolidated.csv"

# Format: iter | f() |treesize | f()/size | heterogeneity | indicator name , indicator code
a = []
all_values = []

iter = []
f = []
treesize = []
ratio = []
heterogeneity = []
indicators = []

####################################################
# Get number of files & create new consolidated file
consolidated = File.new(outputCSVfilename, "w+")
i = 0
j = 0
linebuffer = ""

# For each line
number_of_lines.times do |line_num|
	# For each file with some extension
	linebuffer = ""
	Dir.glob("*.txt") do |file|
		# Open file
		i = 0
		File.open(file, "r") do |fd|
			# Iterate each line
			j = 0
			fd.each_line do |line|
				if (line_num == j && !line.include?("#"))# same line
					# split line
					all_values = line.split("|")
					temp = all_values[5].split(",")[1]

					iter << all_values[0].to_f
					f << all_values[1].to_f
					treesize << all_values[2].to_f
					ratio << all_values[3].to_f
					heterogeneity << all_values[4].to_f
					indicators << temp.to_f
				end
				j += 1
			end # end each line
			i += 1
		end # end open file
	end # end glob

	# sum
  a[0] = iter.reduce(:+).to_f / iter.size.to_f
  a[1] = f.reduce(:+).to_f / f.size.to_f
  a[2] = treesize.reduce(:+).to_f / treesize.size.to_f
  a[3] = ratio.reduce(:+).to_f / ratio.size.to_f
  a[4] = heterogeneity.reduce(:+).to_f / heterogeneity.size.to_f

  freq = []
  freq = indicators.inject(Hash.new(0)) { |h,v| h[v] += 1; h }
  a[5] = indicators.sort_by { |v| freq[v] }.last

  newline = "#{a[0]},#{a[1]},#{a[2]},#{a[3]},#{a[4]},#{a[5]}\n"
  consolidated.write(newline)

	# reset measures
	iter = []
	f = []
	treesize = []
	ratio = []
	heterogeneity = []
	indicators = []
end
