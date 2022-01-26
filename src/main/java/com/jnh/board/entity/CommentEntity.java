package com.jnh.board.entity;

import com.jnh.board.dto.CommentSaveDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    // 게시글 번호 (BoardEntity 연관 관계, 댓글:게시글 = n:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_table")
    private BoardEntity boardEntity;

    // 작성자 (MemberEntity 연관 관계, 댓글:작성자 = n:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_table")
    private MemberEntity memberEntity;

    @Column(length = 20)
    private String commentWriter;

    @Column(length = 100)
    private String commentContents;

    public static CommentEntity toSaveEntity(CommentSaveDTO commentSaveDTO, BoardEntity boardEntity, MemberEntity memberEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(memberEntity.getMemberName());
        commentEntity.setCommentContents(commentSaveDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        commentEntity.setMemberEntity(memberEntity);
        return commentEntity;
    }

}
