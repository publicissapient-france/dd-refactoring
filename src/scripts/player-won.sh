#!/usr/bin/env bash

echo 43 | cat - src/scripts/dungeon.txt | java -classpath target/classes fr.xebia.dd.Dungeon RANDOM
