package TeamProject.TeamProjectWeb.utils;

import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.dto.CommentDTO;

public class ConvertDTO {
    public static CommentDTO convertComment(Comment comment){
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBoardId(comment.getBoard().getId());
        dto.setContent(comment.getContent());
        dto.setFinalDate(comment.getFinalDate());
        dto.setLikeCount(comment.getLikeCount());
        dto.setMemberId(comment.getMember().getId());
        dto.setAuthor(comment.getAuthor());
        return dto;
    }
}
