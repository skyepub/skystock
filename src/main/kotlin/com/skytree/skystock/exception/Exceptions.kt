package com.skytree.skystock.exception

open class BusinessException(message: String) : RuntimeException(message)

class EntityNotFoundException(entity: String, id: Any) :
    BusinessException("${entity}을(를) 찾을 수 없습니다: $id")

class DuplicateException(field: String, value: Any) :
    BusinessException("이미 존재하는 ${field}입니다: $value")

class InvalidStateTransitionException(from: String, to: String) :
    BusinessException("잘못된 상태 전이입니다: $from → $to")
