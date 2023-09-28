package TeamProject.TeamProjectWeb.utils;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.BoardDTO;
import TeamProject.TeamProjectWeb.dto.CommentDTO;
import TeamProject.TeamProjectWeb.dto.MemberDTO;

import java.util.Optional;

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
    public static BoardDTO convertBoard(Board board){
        BoardDTO dto = new BoardDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setAuthor(board.getAuthor());
        dto.setMemberId(board.getMember().getId());
        dto.setFinalDate(board.getFinalDate());
        dto.setBoardKind(board.getBoardKind());
        dto.setViewCnt(board.getViewCnt());
        dto.setLikeCnt(board.getLikeCnt());
        dto.setChatCnt(board.getChatCnt());
        return dto;
    }
    public static MemberDTO convertMember(Member member){
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setStudentId(member.getStudentId());
        dto.setName(member.getName());
        dto.setSchoolName(member.getSchoolName());
        dto.setAccess(member.getAccess());
        dto.setLoginId(member.getLoginId());
        dto.setPassword(member.getPassword());
        dto.setEmail(member.getEmail());
        return dto;
    }
    public static Optional<MemberDTO> convertMember(Optional<Member> optionalMember) {
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            MemberDTO dto = new MemberDTO();
            dto.setId(member.getId());
            dto.setStudentId(member.getStudentId());
            dto.setName(member.getName());
            dto.setSchoolName(member.getSchoolName());
            dto.setAccess(member.getAccess());
            dto.setLoginId(member.getLoginId());
            dto.setPassword(member.getPassword());
            dto.setEmail(member.getEmail());
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
