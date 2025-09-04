# fastapi_spotify_backend.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import sqlite3
from typing import List, Optional

DATABASE = "accountsDatabase.db"
TABLE_NAME = "accounts"

app = FastAPI(title="Spotify Accounts API")

class UserCreate(BaseModel):
    username: str
    password: str
    name: str
    code: Optional[str] = ""

class UserUpdatePassword(BaseModel):
    password: str

class TopTracks(BaseModel):
    tracks: List[str]

# database helper
def get_db_connection():
    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row
    return conn

# create table if not exists
def create_table():
    conn = get_db_connection()
    conn.execute(f"""
        CREATE TABLE IF NOT EXISTS {TABLE_NAME} (
            username TEXT PRIMARY KEY,
            password TEXT,
            name TEXT,
            code TEXT,
            friends TEXT,
            invites TEXT,
            topTracks TEXT
        )
    """)
    conn.commit()
    conn.close()

create_table()

# API endpoints
@app.post("/users/")
def create_user(user: UserCreate):
    conn = get_db_connection()
    try:
        conn.execute(f"""
            INSERT INTO {TABLE_NAME} (username, password, name, code, friends, invites, topTracks)
            VALUES (?, ?, ?, ?, '', '', '')
        """, (user.username, user.password, user.name, user.code))
        conn.commit()
    except sqlite3.IntegrityError:
        raise HTTPException(status_code=400, detail="Username already exists")
    finally:
        conn.close()
    return {"message": "User created successfully"}

@app.get("/users/{username}")
def get_user(username: str):
    conn = get_db_connection()
    user = conn.execute(f"SELECT * FROM {TABLE_NAME} WHERE username = ?", (username,)).fetchone()
    conn.close()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return dict(user)

@app.put("/users/{username}/password")
def update_password(username: str, update: UserUpdatePassword):
    conn = get_db_connection()
    conn.execute(f"UPDATE {TABLE_NAME} SET password = ? WHERE username = ?", (update.password, username))
    conn.commit()
    conn.close()
    return {"message": "Password updated successfully"}

@app.put("/users/{username}/toptracks")
def save_top_tracks(username: str, toptracks: TopTracks):
    tracks_str = ",".join(toptracks.tracks)
    conn = get_db_connection()
    conn.execute(f"UPDATE {TABLE_NAME} SET topTracks = ? WHERE username = ?", (tracks_str, username))
    conn.commit()
    conn.close()
    return {"message": "Top tracks updated successfully"}

@app.delete("/users/{username}")
def delete_user(username: str):
    conn = get_db_connection()
    conn.execute(f"DELETE FROM {TABLE_NAME} WHERE username = ?", (username,))
    conn.commit()
    conn.close()
    return {"message": "User deleted successfully"}

@app.get("/users/{username}/toptracks")
def get_top_tracks(username: str):
    conn = get_db_connection()
    user = conn.execute(f"SELECT topTracks FROM {TABLE_NAME} WHERE username = ?", (username,)).fetchone()
    conn.close()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    tracks = user["topTracks"].split(",") if user["topTracks"] else []
    return {"topTracks": tracks}

