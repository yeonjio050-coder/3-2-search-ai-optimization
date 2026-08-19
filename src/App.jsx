import './App.css'

function App() {
  return (
    <>
      <header className="site-header">
        <a className="logo" href="/">VAN</a>
        <nav aria-label="주요 메뉴">
          <ul>
            <li><a href="#news">뉴스</a></li>
            <li><a href="#about">소개</a></li>
            <li><a href="#contact">문의</a></li>
          </ul>
        </nav>
      </header>

      <main>
        <h1>VAN 뉴스</h1>

        <section id="news" aria-labelledby="news-heading">
          <h2 id="news-heading">최신 기사</h2>

          <article>
            <h3>첫 번째 기사 제목</h3>
            <p>
              기사 요약 내용이 들어가는 자리입니다. 검색엔진과 생성형 AI가
              문서 구조를 정확히 이해할 수 있도록 시맨틱 태그로 구성했습니다.
            </p>
            <a href="/news/1">기사 전문 읽기</a>
          </article>

          <article>
            <h3>두 번째 기사 제목</h3>
            <p>
              각 기사는 article 태그로 감싸 독립적인 콘텐츠 단위임을
              명시합니다.
            </p>
            <a href="/news/2">기사 전문 읽기</a>
          </article>
        </section>

        <section id="about" aria-labelledby="about-heading">
          <h2 id="about-heading">VAN 소개</h2>
          <p>
            VAN은 신설 언론사의 검색·AI 노출 최적화를 담당하는 프로젝트입니다.
          </p>
        </section>
      </main>

      <footer id="contact">
        <h2>문의</h2>
        <address>
          이메일: <a href="mailto:contact@van.example">contact@van.example</a>
        </address>
        <p><small>&copy; 2026 VAN. All rights reserved.</small></p>
      </footer>
    </>
  )
}

export default App