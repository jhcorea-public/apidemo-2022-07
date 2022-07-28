package org.practice.haeng.apidemo.search.webapp.collector.compaction;

/**
 * 크기가 n인 m개의 데이터 리스트 사이의 중복을 제거해서 하나의 리스트를 만들때
 * 데이터의 동치 여부가 여러개의 데이터의 조합으로 확인되어야 할때, equal 연산으로 합치기 위해서는 O(n^m)만큼 걸린다.
 *
 * 후보키를 이용해서 동치 가능성이 있는 데이터 세트를 만든 후, 해당 세트 안에서 동치 비교를 한다.
 *
 * 해당 구조에 대한 자세한 아이디어는 README.md에 기록
 */
public interface DataEqualChecker {

    //후보키, 후보키가 같으면 같은 데이터다 (거짓) but 같은 데이터면 후보키가 같다 (참)
    String getKey();

    // 두 데이터가 같은지 여부, object.equal을 오버라이딩하는것보다 명시적으로 구현을 강제하는 방향으로 간다.
    boolean equalData(Object other);

}