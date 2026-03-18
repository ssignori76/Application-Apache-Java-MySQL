#!/bin/bash

# Script per build e push delle immagini su Docker Hub (lusig76)

# Effettua il login se necessario (commentato, fallo tu una volta)
# docker login -u lusig76

echo "--- Building images ---"
docker-compose build

echo "--- Pushing images to Docker Hub (lusig76) ---"
docker-compose push backend
docker-compose push frontend

echo "--- Done ---"
