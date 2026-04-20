-- ==========================================
-- MinusOne Server: Initial Seed Data
-- ==========================================

-- 1. Create a Test Account (Password is MD5 of '123456' -> e10adc3949ba59abbe56e057f20f883e)
INSERT INTO accounts (id, user_name, phone, password)
VALUES (1, 'testuser', '13800138000', 'e10adc3949ba59abbe56e057f20f883e')
    ON DUPLICATE KEY UPDATE user_name=user_name;

-- 2. Create the associated Profile (Shares ID with Account)
INSERT INTO profiles (user_id, nickname, avatar_url, background_url)
VALUES (1, 'MinusOne Tester', 'http://p1.music.126.net/SUeqMM8HOIpHv9Nhl9qt9w==/109951165647004069.jpg', NULL)
    ON DUPLICATE KEY UPDATE nickname=nickname;

-- 3. Create the associated User (Shares ID with Account/Profile)
INSERT INTO users (id, nickname, create_time, listen_songs)
VALUES (1, 'MinusOne Tester', 1672531200000, 42)
    ON DUPLICATE KEY UPDATE nickname=nickname;

-- 4. Create Comment Threads for Songs
INSERT INTO comment_threads (id, resource_id) VALUES (1, 'R_SO_4_1001') ON DUPLICATE KEY UPDATE resource_id=resource_id;
INSERT INTO comment_threads (id, resource_id) VALUES (2, 'R_SO_4_1002') ON DUPLICATE KEY UPDATE resource_id=resource_id;

-- 5. Create Songs
-- Note: 1001 maps to the file in your minusone-media/originals/1001.mp3
INSERT INTO songs (id, name, file_path, comment_thread_id)
VALUES (1001, 'Test Song - MinusOne Theme', 'minusone-media/originals/1001.mp3', 1)
    ON DUPLICATE KEY UPDATE name=name;

INSERT INTO songs (id, name, file_path, comment_thread_id)
VALUES (1002, 'Never Gonna Give You Up', NULL, 2)
    ON DUPLICATE KEY UPDATE name=name;

-- 6. Create Artists
INSERT INTO artists (id, name) VALUES (101, 'MinusOne Band') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO artists (id, name) VALUES (102, 'Rick Astley') ON DUPLICATE KEY UPDATE name=name;

-- 7. Link Songs to Artists (Many-to-Many)
INSERT IGNORE INTO song_artists (song_id, artist_id) VALUES (1001, 101);
INSERT IGNORE INTO song_artists (song_id, artist_id) VALUES (1002, 102);

-- 8. Create a 'STAR' (Liked Songs) Playlist for the User
INSERT INTO playlists (id, name, special_type, user_id)
VALUES (10001, '我喜欢的音乐', 'STAR', 1)
    ON DUPLICATE KEY UPDATE name=name;

-- 9. Add Songs to the Playlist
INSERT IGNORE INTO playlist_tracks (playlist_id, song_id) VALUES (10001, 1001);
INSERT IGNORE INTO playlist_tracks (playlist_id, song_id) VALUES (10001, 1002);

-- 10. Add a Comment to the first song
INSERT INTO comments (id, thread_id, user_id, content, liked_count, create_time)
VALUES (1, 1, 1, 'This is a test comment for the MinusOne Theme!', 99, CURRENT_TIMESTAMP)
    ON DUPLICATE KEY UPDATE content=content;

-- 11. Grant basic Privileges for the songs (NCM clients check this to see if a song is playable)
INSERT IGNORE INTO privileges (id) VALUES (1001);
INSERT IGNORE INTO privileges (id) VALUES (1002);