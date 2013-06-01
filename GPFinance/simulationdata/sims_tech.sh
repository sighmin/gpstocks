#!/usr/bin/env bash

# Script to run GP sims for technical indicators for all quarters, 30 times

for q in 1 2 3 4
do
  for i in {0..29}
  do
    echo "java -jar GPFinance.jar run type=technical financialQuarter=$q > ../simulationdata/technical/fundamental-quarter$q-sample$i.txt"
  done
done