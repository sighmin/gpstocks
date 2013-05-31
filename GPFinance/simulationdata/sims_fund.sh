#!/usr/bin/env bash

# Script to run GP sims for fundamental indicators for all quarters, 30 times

java -jar GPFinance.jar run type=fundamental financialQuarter=1 > fundamental_quarter1_0.txt

for q in 1 2 3 4
do
  for i in {0..29}
  do
    echo "java -jar GPFinance.jar run type=fundamental financialQuarter=$q > fundamental_quarter$q_$i.txt"
  done
done