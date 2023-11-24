package sejong.capstone.backjoonrecommend.util;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import sejong.capstone.backjoonrecommend.domain.Code;
import sejong.capstone.backjoonrecommend.repository.CodeRepository;

@Component
public class Init {

    private final CodeRepository codeRepository;

    public Init(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @PostConstruct
    public void Innit() {
        Code code = new Code();
        code.setCode("\"#include <iostream>\n"
                + "#include <list>\n"
                + "#include <vector>\n"
                + "#include <algorithm>\n"
                + "#include <string>\n"
                + "#include <cstring>\n"
                + "using namespace std;\n"
                + "\n"
                + "struct Student {\n"
                + "\tchar name[11];\n"
                + "\tint kor, eng, math;\n"
                + "\n"
                + "\tconst bool operator<(const Student& a) const {\n"
                + "\t\tif (kor != a.kor) return kor > a.kor;\n"
                + "\t\tif (eng != a.eng) return eng < a.eng;\n"
                + "\t\tif (math != a.math) return math > a.math;\n"
                + "\t\treturn strcmp(name, a.name) < 0;\n"
                + "\t\t// string 에서는 compare 사용 string1.compare(string2)\n"
                + "\t}\n"
                + "};\n"
                + "\n"
                + "int main() {\n"
                + "\tstd::cin.tie(nullptr)->sync_with_stdio(false);\n"
                + "\tint N, x, y;\n"
                + "\tcin >> N;\n"
                + "\tStudent *stu = new Student[N];\n"
                + "\tfor (int i = 0; i < N; i++) {\n"
                + "\t\tcin >> stu[i].name >> stu[i].kor >> stu[i].eng >> stu[i].math;\n"
                + "\t}\n"
                + "\tstring ans;\n"
                + "\tans.reserve(N * 11);\n"
                + "\tstd::sort(stu, stu + N);\n"
                + "\tfor (int i = 0; i < N; i++) {\n"
                + "\t\tans.append(stu[i].name);\n"
                + "\t\tans.push_back('\\n');\n"
                + "\t}\n"
                + "\tstd::cout << ans;\n"
                + "\tdelete[] stu;\n"
                + "\treturn 0;\n"
                + "}\"");
        code.setLanguage("C++17");
        code.setTime(40L);
        code.setMemory(5444L);
        code.setNumber(10825L);
        codeRepository.save(code);

        code = new Code();
        code.setCode("#include <iostream>\n"
                + "#include <algorithm>\n"
                + "using namespace std;\n"
                + "\n"
                + "class student{\n"
                + "public:\n"
                + "\tchar name[10];\n"
                + "\tint kor;\n"
                + "\tint eng;\n"
                + "\tint math;\n"
                + "};\n"
                + "\n"
                + "bool compare(student a, student b){\n"
                + "\tif(a.kor != b.kor)\n"
                + "\t\treturn a.kor > b.kor;\n"
                + "\telse if(a.eng != b.eng)\n"
                + "\t\treturn a.eng < b.eng;\n"
                + "\telse if(a.math != b.math)\n"
                + "\t\treturn a.math > b.math;\n"
                + "\telse{\n"
                + "\t\tint i=0;\n"
                + "\t\twhile(1){\n"
                + "\t\t\tif(a.name[i] != b.name[i])\n"
                + "\t\t\t\treturn a.name[i] < b.name[i];\n"
                + "\t\t\telse\n"
                + "\t\t\t\ti++;\n"
                + "\t\t}\n"
                + "\t}\n"
                + "}\n"
                + "\n"
                + "int main(){\n"
                + "\tios::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);\n"
                + "\tint num;\n"
                + "\tstudent * arr;\n"
                + "\n"
                + "\tcin>>num;\n"
                + "\tarr = new student[num];\n"
                + "\tfor(int i=0; i<num; i++){\n"
                + "\t\tcin>>arr[i].name;\n"
                + "\t\tcin>>arr[i].kor;\n"
                + "\t\tcin>>arr[i].eng;\n"
                + "\t\tcin>>arr[i].math;\n"
                + "\t}\n"
                + "\tsort(arr, arr+num, compare);\n"
                + "\n"
                + "\tfor(int i=0; i<num; i++)\n"
                + "\t\tcout<<arr[i].name<<'\\n';\n"
                + "\n"
                + "\tdelete []arr;\n"
                + "\treturn 0;\n"
                + "}");
        code.setLanguage("C++17");
        code.setTime(52L);
        code.setMemory(4368L);
        code.setNumber(10825L);
        codeRepository.save(code);
    }
}
