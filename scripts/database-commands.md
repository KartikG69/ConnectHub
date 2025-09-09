# ConnectHub Database Commands

## Setup Instructions

### 1. Run in WSL Terminal:
```bash
cd /mnt/c/Users/lenovo/AndroidStudioProjects/ConnectHub
chmod +x scripts/setup-database.sh
./scripts/setup-database.sh
```

### 2. Verify Setup:
```bash
# Check if containers are running
docker ps

# Connect to PostgreSQL
docker exec -it connecthub_postgres psql -U connecthub_user -d connecthub

# View database tables
\dt

# Exit PostgreSQL
\q
```

### 3. Access PgAdmin:
- URL: http://localhost:8080
- Email: admin@connecthub.com
- Password: admin123

## Useful Docker Commands

### Start/Stop Services:
```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Restart services
docker-compose restart

# View logs
docker-compose logs -f postgres
```

### Database Operations:
```bash
# Connect to database
docker exec -it connecthub_postgres psql -U connecthub_user -d connecthub

# Run SQL file
docker exec -i connecthub_postgres psql -U connecthub_user -d connecthub < database/migration.sql

# Backup database
docker exec connecthub_postgres pg_dump -U connecthub_user connecthub > backup.sql

# Restore database
docker exec -i connecthub_postgres psql -U connecthub_user -d connecthub < backup.sql
```

### Clean Reset:
```bash
# Stop and remove everything
docker-compose down -v --remove-orphans

# Remove volumes
docker volume prune

# Start fresh
./scripts/setup-database.sh
```

## Firebase Data Connect Setup

### 1. Install Firebase CLI:
```bash
# In WSL
curl -sL https://firebase.tools | bash

# Or using npm
npm install -g firebase-tools
```

### 2. Login to Firebase:
```bash
firebase login
```

### 3. Initialize Data Connect:
```bash
cd /mnt/c/Users/lenovo/AndroidStudioProjects/ConnectHub
firebase init dataconnect
```

### 4. Deploy Data Connect:
```bash
firebase deploy --only dataconnect
```

## Testing Database Connection

### Sample SQL Queries:
```sql
-- Check users
SELECT * FROM users;

-- Check conversations
SELECT * FROM conversations;

-- Check messages with sender names
SELECT m.content, u.name as sender_name, m.created_at 
FROM messages m 
JOIN users u ON m.sender_id = u.id 
ORDER BY m.created_at DESC;

-- Check call history
SELECT c.call_type, c.status, c.duration_seconds,
       caller.name as caller_name, 
       receiver.name as receiver_name,
       c.started_at
FROM calls c
JOIN users caller ON c.caller_id = caller.id
JOIN users receiver ON c.receiver_id = receiver.id
ORDER BY c.started_at DESC;
```

## Connection Strings

### For Android Development:
```
Database URL: postgresql://connecthub_user:connecthub_password@10.0.2.2:5432/connecthub
```

### For Local Development:
```
Database URL: postgresql://connecthub_user:connecthub_password@localhost:5432/connecthub
```

## Troubleshooting

### Common Issues:

1. **Port 5432 already in use:**
   ```bash
   sudo lsof -i :5432
   sudo pkill -f postgres
   ```

2. **Permission denied:**
   ```bash
   sudo chmod +x scripts/setup-database.sh
   ```

3. **Docker not found:**
   ```bash
   # Install Docker in WSL
   curl -fsSL https://get.docker.com -o get-docker.sh
   sudo sh get-docker.sh
   sudo usermod -aG docker $USER
   ```

4. **Container won't start:**
   ```bash
   docker-compose logs postgres
   docker system prune -f
   ```