#!/bin/sh
docker_host_ip=$(ip route show | awk '/default/ {print $3}')
echo $docker_host_ip
