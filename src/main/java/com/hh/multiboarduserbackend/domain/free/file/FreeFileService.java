package com.hh.multiboarduserbackend.domain.free.file;

import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import com.hh.multiboarduserbackend.exception.FileErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class FreeFileService {

    private final FreeFileRepository freeFileRepository;

    /**
     * 파일 리스트 저장
     * @param fileList - 파일 리스트
     */
    public void saveFileList(List<FileVo> fileList) {
        freeFileRepository.saveAll(fileList);
    }

    /**
     * 파일 아이디로 파일 조회
     * @param freeFileId - pk
     * @return - 파일 정보
     */
    public FileResponseDto findById(Long freeFileId) {
        Optional<FileVo> fileVo = Optional.ofNullable(freeFileRepository.findById(freeFileId));
        return FileResponseDto.toDto(fileVo.orElseThrow(() -> FileErrorCode.FILE_NOT_FOUND.defaultException()));
    }

    /**
     * 게시글 아이디로 파일 리스트 조회
     * @param freePostId - 게시글 아이디
     * @return - 파일 정보 리스트
     */
    public List<FileResponseDto> findAllByPostId(Long freePostId) {
        List<FileVo> fileList = freeFileRepository.findAllByPostId(freePostId);
        return fileList.stream()
                .map(FileResponseDto::toDto).collect(toList());
    }

    /**
     * 파일 아이디 리스트로 파일 리스트 조회
     * @param idList - pk list
     * @return - 파일 정보 리스트
     */
    public List<FileResponseDto> findAllByIds(List<Long> idList) {
        List<FileVo> fileList = freeFileRepository.findAllByIds(idList);
        return fileList.stream()
                .map(FileResponseDto::toDto).collect(toList());
    }

    /**
     * 파일 아이디 리스트 받아서 삭제
     * @param idList - pk list
     */
    public void deleteAllByIds(List<Long> idList) {
        freeFileRepository.deleteAllByIds(idList);
    }
}
