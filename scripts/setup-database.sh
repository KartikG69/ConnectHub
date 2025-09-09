#!/bin/bash

# ConnectHub Database Setup Script
# Run this in WSL to setup PostgreSQL with Docker

set -e

echo "🚀 Setting up ConnectHub PostgreSQL Database..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Navigate to project directory
cd "$(dirname "$0")/.."

# Stop existing containers if running
echo "🛑 Stopping existing containers..."
docker-compose down --remove-orphans

# Remove existing volumes for fresh start
echo "🗑️  Removing existing volumes..."
docker volume rm connecthub_postgres_data connecthub_pgadmin_data 2>/dev/null || true

# Start PostgreSQL and PgAdmin
echo "🐘 Starting PostgreSQL and PgAdmin..."
docker-compose up -d

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
timeout 60 bash -c 'until docker exec connecthub_postgres pg_isready -U connecthub_user -d connecthub; do sleep 2; done'

if [ $? -eq 0 ]; then
    echo "✅ PostgreSQL is ready!"
    echo ""
    echo "📊 Database Information:"
    echo "  - Database URL: postgresql://connecthub_user:connecthub_password@localhost:5432/connecthub"
    echo "  - PgAdmin URL: http://localhost:8080"
    echo "  - PgAdmin Email: admin@connecthub.com"
    echo "  - PgAdmin Password: admin123"
    echo ""
    echo "🔧 To connect to database directly:"
    echo "  docker exec -it connecthub_postgres psql -U connecthub_user -d connecthub"
    echo ""
    echo "📝 To view logs:"
    echo "  docker-compose logs -f postgres"
    echo ""
    echo "🛑 To stop services:"
    echo "  docker-compose down"
else
    echo "❌ PostgreSQL failed to start properly"
    echo "📝 Check logs with: docker-compose logs postgres"
    exit 1
fi