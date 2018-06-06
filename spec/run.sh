#!/usr/bin/env bash
newman run Conduit.postman_collection.json -e Conduit.postman_environment.json --bail
