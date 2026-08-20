import { Helmet } from 'react-helmet-async'

const SITE_NAME = 'VAN 뉴스'
const SITE_URL = 'https://van.example.com'
const DEFAULT_DESCRIPTION =
  'VAN 뉴스는 신설 언론사의 검색·AI 노출 최적화 프로젝트입니다. 최신 기사와 소식을 전합니다.'

/**
 * 페이지별 메타데이터를 주입하는 전역 SEO 컴포넌트
 * 3-2 검색·AI 노출 최적화 / SEO-001, SEO-003, SEO-004, SEO-005
 */
function Seo({
  title,
  description = DEFAULT_DESCRIPTION,
  path = '/',
  type = 'website',
  image,
  noindex = false,
  jsonLd,
}) {
  const fullTitle = title ? `${title} | ${SITE_NAME}` : SITE_NAME
  const canonicalUrl = `${SITE_URL}${path}`

  return (
    <Helmet>
      <html lang="ko" />
      <title>{fullTitle}</title>
      <meta name="description" content={description} />
      <link rel="canonical" href={canonicalUrl} />
      <meta
        name="robots"
        content={noindex ? 'noindex, nofollow' : 'index, follow'}
      />

      <meta property="og:type" content={type} />
      <meta property="og:site_name" content={SITE_NAME} />
      <meta property="og:title" content={fullTitle} />
      <meta property="og:description" content={description} />
      <meta property="og:url" content={canonicalUrl} />
      <meta property="og:locale" content="ko_KR" />
      {image && <meta property="og:image" content={image} />}

      <meta name="twitter:card" content="summary_large_image" />
      <meta name="twitter:title" content={fullTitle} />
      <meta name="twitter:description" content={description} />
      {image && <meta name="twitter:image" content={image} />}

      {jsonLd && (
        <script type="application/ld+json">{JSON.stringify(jsonLd)}</script>
      )}
    </Helmet>
  )
}

export default Seo