package com.github.bumblebee202111.minusone.server.entity

import com.github.bumblebee202111.minusone.server.constant.api.ApiResourceConstants
import jakarta.persistence.*

@Entity
@Table(name = "songs")
data class Song(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long=0L,

    var name:String,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "song_artists",
        joinColumns = [JoinColumn(name = "song_id")],
        inverseJoinColumns = [JoinColumn(name = "artist_id")]
    )
    val artists: MutableSet<Artist> = mutableSetOf(),

    var filePath: String? = null,

    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "comment_thread_id", referencedColumnName = "id")
    var commentThread: CommentThread? = null
) {
    @PostPersist
    fun initializeCommentThread() {
        if (this.commentThread == null) {
            this.commentThread = CommentThread(resourceId = "${ApiResourceConstants.THREAD_PREFIX_SONG}${this.id}")
        }
    }
}
