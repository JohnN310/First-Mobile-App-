import sqlite3
from fastapi_spotify_backend import *


conn = sqlite3.connect("accountsDatabase.db")
cursor = conn.cursor()

# List tables
cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
print(cursor.fetchall())

# Check contents of accounts table
cursor.execute("SELECT * FROM accounts;")
print(cursor.fetchall())

conn.close()

