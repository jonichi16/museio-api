#!/bin/bash

# Exit immediately if a command exits with a non-zero status, an unset variable is used, or a pipeline fails
set -euf -o pipefail

# Display usage information
usage() {
    echo ""
    echo "Usage: $0 {lint|docs|dev|build|logs|stop}"
    echo ""
    echo "Available commands:"
    echo "  lint  - Run code quality checks (Checkstyle, PMD, SpotBugs)"
    echo "  docs  - Generate API documentation"
    echo "  dev   - Start development environment with PostgreSQL only"
    echo "  build - Build and start the application (production setup)"
    echo "  logs  - Follow logs for the 'app' service"
    echo "  stop  - Stop all running containers and clean up unused images"
    echo ""
    exit 1
}

# Check if an argument is provided
if [[ $# -eq 0 ]]; then
    echo "Error: No command provided."
    usage
fi

case "$1" in
    lint)
        echo "Starting code quality checks..."

        echo "Step 1: Cleaning previous build artifacts..."
        ./mvnw clean

        echo "Step 2: Running Checkstyle..."
        ./mvnw checkstyle:check -ntp

        echo "Step 3: Running PMD analysis..."
        ./mvnw pmd:check -ntp

        echo "Step 4: Running SpotBugs analysis..."
        ./mvnw spotbugs:check -ntp

        echo "Step 5: Cleaning up after checks..."
        ./mvnw clean

        echo "✅  Code quality checks completed successfully."
        ;;
    docs)
        echo "Generating API documentation..."
        ./mvnw clean package -Dtest=*IntegrationTest -Djacoco.skip=true -Dsurefire.failIfNoSpecifiedTests=false

        echo "✅  API documentation generated successfully."
        ;;
    dev)
        echo "Starting development environment with PostgreSQL only..."
        docker compose -f docker-compose.yml --project-name museio up -d
        echo "✅  Development environment started. Use 'docker ps' to verify running containers."
        ;;
    build)
        echo "Building and starting the application (production setup)..."
        docker compose -f docker-compose.yml -f docker-compose.build.yml --project-name museio up --build -d
        echo "✅  Application built and started. Use 'docker ps' to verify running containers."
        ;;
    logs)
        echo "Tailing logs for the 'app' service..."
        docker logs -f api
        ;;
    stop)
        echo "Stopping all services and cleaning up..."
        docker compose --project-name museio down
        echo "Removing unused Docker images..."
        docker image prune -f
        echo "✅  All services stopped and cleanup completed."
        ;;
    *)
        echo "Error: Invalid command '$1'"
        usage
        ;;
esac
