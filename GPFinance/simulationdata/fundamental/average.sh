#!/usr/bin/env bash

# Script to run the extract script on all files in this directory

mkdir averages

for f in *.txt
do
	ruby average.rb $f > averages/"$f.csv"
done
