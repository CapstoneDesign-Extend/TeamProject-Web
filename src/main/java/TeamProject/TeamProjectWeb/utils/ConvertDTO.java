package TeamProject.TeamProjectWeb.utils;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.BoardDTO;
import TeamProject.TeamProjectWeb.dto.CommentDTO;
import TeamProject.TeamProjectWeb.dto.LikeDTO;
import TeamProject.TeamProjectWeb.dto.MemberDTO;

import java.util.ArrayList;
import java.util.List;
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
    public static List<BoardDTO> convertBoardList(List<Board> boards){
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boards){
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
            boardDTOList.add(dto);
        }

        return boardDTOList;
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
    public static List<MemberDTO> convertMemberList(List<Member> members){
        List<MemberDTO> memberDTOS = new ArrayList<>();
        for (Member member : members){
            MemberDTO dto = new MemberDTO();
            dto.setId(member.getId());
            dto.setStudentId(member.getStudentId());
            dto.setName(member.getName());
            dto.setSchoolName(member.getSchoolName());
            dto.setAccess(member.getAccess());
            dto.setLoginId(member.getLoginId());
            dto.setPassword(member.getPassword());
            dto.setEmail(member.getEmail());
            memberDTOS.add(dto);
        }
        return memberDTOS;
    }
    public static LikeDTO convertLike(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setLikeId(like.getId());
        dto.setMemberId(like.getMember().getId());
        if (like.getBoard() != null) {
            dto.setBoardId(like.getBoard().getId());
        }
        if (like.getComment() != null) {
            dto.setCommentId(like.getComment().getId());
        }
        return dto;
    }
}
