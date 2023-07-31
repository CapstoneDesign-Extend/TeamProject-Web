package TeamProject.TeamProjectWeb.controller.timetable;

import TeamProject.TeamProjectWeb.domain.Timetable;
import TeamProject.TeamProjectWeb.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    // 시간표 생성 폼을 보여줍니다.
    @GetMapping("/create")
    public String createForm(Model model) {
        // 시간표 생성을 위해 빈 Timetable 객체를 모델에 추가합니다.
        model.addAttribute("timetable", new Timetable());
        return "timetable/createForm";
    }

    // 시간표 생성 처리를 합니다.
    @PostMapping("/create")
    public String create(@ModelAttribute Timetable timetable) {
        // 전달받은 시간표 정보를 서비스를 통해 저장합니다.
        timetableService.createTimetable(timetable);
        return "redirect:/timetable/list";
    }

    // 모든 시간표 목록을 보여줍니다.
    @GetMapping("/list")
    public String list(Model model) {
        // 모든 시간표 정보를 조회하여 모델에 추가합니다.
        List<Timetable> timetables = timetableService.findAllTimetables();
        model.addAttribute("timetables", timetables);
        return "timetable/list";
    }

    // 시간표 수정 폼을 보여줍니다.
    @GetMapping("/update/{year}/{semester}")
    public String updateForm(@PathVariable int year, @PathVariable int semester, Model model) {
        // 특정 연도와 학기의 시간표 정보를 조회하여 수정 폼에 기본값으로 설정합니다.
        Timetable timetable = timetableService.findByYearAndSemester(year, semester);
        model.addAttribute("timetable", timetable);
        return "timetable/updateForm";
    }

    // 시간표 수정 처리를 합니다.
    @PostMapping("/update")
    public String update(@ModelAttribute Timetable timetable) {
        // 전달받은 시간표 정보를 서비스를 통해 수정합니다.
        timetableService.updateTimetable(timetable);
        return "redirect:/timetable/list";
    }

    // 시간표 삭제 처리를 합니다.
    @PostMapping("/delete/{year}/{semester}")
    public String delete(@PathVariable int year, @PathVariable int semester) {
        // 특정 연도와 학기의 시간표를 삭제합니다.
        timetableService.deleteTimetable(year, semester);
        return "redirect:/timetable/list";
    }

}